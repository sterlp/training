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
  error: any;
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


export function useFetch<T>(
    initialUrl: string,
    initial: T
  ): [Signal<T>, boolean, Signal<string>] {
    const [] = useReducer();
    const url = useSignal<string>(initialUrl);
    const data = useSignal<T>(initial);
    const error = useSignal(null);
    const [isLoading, setLoading] = useState(false);
  
    useEffect(() => {
      if (url.value && url.value != "") {
        batch(() => {
          error.value = null;
          setLoading(true);
        });
        fetch(url.value)
          .then((r) => r.json())
          .then((r) => (data.value = r as T))
          .catch((e) => (error.value = e))
          .finally(() => setLoading(false));
      } else {
        batch(() => {
          error.value = null;
          data.value = null;
          setLoading(false);
        });
      }
    }, [url.value, data, error]);
  
    return [data, isLoading, url];
  }