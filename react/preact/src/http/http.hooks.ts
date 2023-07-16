import { useEffect, useReducer } from "preact/hooks";

export enum FetchActionType {
  START,
  DONE,
  ERROR
}
export interface FetchAction<ResultType> {
  type: FetchActionType
  result?: ResultType
  error?: Response | unknown
}
export interface FetchState<ResultType> {
  result?: ResultType;
  isLoading: boolean;
  hasError: boolean;
  error?: Response | unknown;
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
          result: action.result
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
    initial?: S
  ): [FetchState<S>, (url?: string) => Promise<S | Response | undefined>] {

    const [state, dispatchState] = useReducer<FetchState<S>, FetchAction<S>, S>(fetchReducer, initial, emptyFetchState);
  
    const doReload = (newUrl?: string): Promise<S | undefined> => {
      dispatchState({type: FetchActionType.START});
      return fetch(newUrl ?? url)
        .then(async (response) => {
          let result;
          try {
            if (response.ok) {
              const body = await response.text();
              const hashBody = body && body.length > 0; // assuming always a JSON response
              if (hashBody) result = JSON.parse(body);
              dispatchState({type: FetchActionType.DONE, result});  
            } else {
              dispatchState({type: FetchActionType.ERROR, error: response});  
            }
          } catch (e) {
            dispatchState({type: FetchActionType.ERROR, error: e});
          }
          return result;
        })
        .catch((e) => dispatchState({type: FetchActionType.ERROR, error: e}));
    }
    useEffect(() => {
      doReload();
    }, []);
  
    return [state, doReload];
  }