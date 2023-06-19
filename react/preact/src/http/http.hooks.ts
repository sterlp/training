import { Signal, batch, useSignal } from "@preact/signals";
import { useEffect, useState, useReducer } from "preact/hooks";

export enum FetchActionType {
  START,
  DONE,
  ERROR
}
export interface FetchAction<ResultType> {
  type: FetchActionType
  result?: ResultType
  error?: any
}
export interface FetchState<ResultType> {
  result?: ResultType;
  isLoading: boolean;
  hasError: boolean;
  error?: any;
}

export function emptyFetchState<S>(state?: S): FetchState<S> {
  return {
    result: state,
    isLoading: false,
    hasError: false
  }
}

export function fetchReducer<S>(state: FetchState<S>, action: FetchAction<S>): FetchState<S> {
  switch(action.type) {
    case FetchActionType.START:
      return {
        ...state,
        isLoading: true,
        hasError: false,
        error: undefined
      }
      case FetchActionType.DONE:
        return {
          ...state,
          isLoading: false,
          result: state.result
        }
      case FetchActionType.ERROR:
        return {
          ...state,
          hasError: true,
          isLoading: false,
          error: action.error,
        }
    default: return state;
  }
}


export function useFetch<S>(
    url: string,
    initial: S
  ): [Signal<S>, boolean, (url?: string) => Promise<S>] {

    const [state, dispatchState] = useReducer<FetchState<S>, FetchAction<S>, S>(fetchReducer, initial, emptyFetchState);
  
    const doReload = (newUrl?: string): Promise<S | undefined> => {
      dispatchState({type: FetchActionType.START});
      return fetch(newUrl ?? url)
        .then((r) => r.json())
        .then((result) => {
          dispatchState({type: FetchActionType.DONE, result});
          return result;
        })
        .catch((e) => dispatchState({type: FetchActionType.ERROR, error: e}));
    }
    useEffect(() => {
      doReload();
    }, []);
  
    return [state, doReload];
  }