
## Use the fetch API with Singals in a React way

[see Preact Code](./preact/src/routes/profile/index.tsx)

```typescript
function useFetch<T>(initialUrl: string, initial: T)
    : [Signal<T>, boolean, Signal<string>] {
  
  const url = useSignal(initialUrl);
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
  }, [url.value]);

  return [data, isLoading, url];
}
```