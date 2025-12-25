import { Signal, batch, effect, signal, useSignal } from "@preact/signals";
import { h } from "preact";
import { useEffect, useState } from "preact/hooks";
import { Button, Card } from "react-bootstrap";

interface Props {
  user: string;
}
//const count = signal(1);
const time = signal(Date.now());

// Note: `user` comes from the URL, courtesy of our router
const Profile = ({ user }: Props) => {
  const count = useSignal(1);

  effect(() => {
    const timer = setInterval(() => (time.value = Date.now()), 1000);
    return () => clearInterval(timer);
  });

  const {data, isLoading, url} = useFetch<Array<any>>(
    "https://jsonplaceholder.typicode.com/todos",
    []
  );
  console.info("render Profile", new Date());

  return (
    <Card>
      <Card.Body>
        <Card.Title>Profile: {user}</Card.Title>
        <Foo count={count} data={data} isLoading={isLoading} user={user} />
        <Button
          className="me-1"
          variant="primary"
          onClick={() => (count.value = count.value + 1)}
        >
          Click Me
        </Button>
        <Button
          className="me-1"
          variant="primary"
          onClick={() =>
            (url.value = "https://jsonplaceholder.typicode.com/albums")
          }
        >
          Load albums
        </Button>
        <Button
          className="me-1"
          variant="primary"
          onClick={() =>
            (url.value = "https://jsonplaceholder.typicode.com/todos")
          }
        >
          Load ToDos
        </Button>
      </Card.Body>
    </Card>
  );
};

const Foo = ({user, count, isLoading, data}) => {
    console.info("Foo rendered", new Date());
    return (
        <Card.Text>
          This is the user profile for a user named {user}.
          <p>Current time: {new Date(time.value).toLocaleString()}</p>
          <p>Clicked {count} times.</p>
          <p>{isLoading ? "Loading ..." : "Result: " + data.value.length}</p>
        </Card.Text>
    );
}

function useFetch<T>(
  initialUrl: string,
  initial: T
)  {
  const url = useSignal(initialUrl);
  const data = useSignal<T>(initial);
  const error = useSignal(null);
  const isLoading = useSignal(false);

  useEffect(() => {
    if (url.value && url.value != "") {
      batch(() => {
        error.value = null;
        isLoading.value = false;
      });
      fetch(url.value)
        .then((r) => r.json())
        .then((r) => (data.value = r as T))
        .catch((e) => (error.value = e))
        .finally(() => isLoading.value = false);
    } else {
      batch(() => {
        error.value = null;
        data.value = null;
        isLoading.value = false;
      });
    }
  }, [url.value, data, error]);

  return {data, isLoading, url};
}

export default Profile;
