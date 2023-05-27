import { Signal, batch, useSignal } from "@preact/signals";
import { useEffect, useState } from "preact/hooks";


export function useFetch<T>(
    initialUrl: string,
    initial: T
  ): [Signal<T>, boolean, Signal<string>] {

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