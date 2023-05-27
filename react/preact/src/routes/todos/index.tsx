import { h, Fragment } from "preact";
import { Button, Card, ListGroup, Spinner } from "react-bootstrap";
import { useFetch } from "src/components/http.effects";
import { ToDosList } from "./todos.model";

interface Props {}

// Note: `user` comes from the URL, courtesy of our router
const TodosPage = ({}: Props) => {
  const [data, isLoading, url] = useFetch<ToDosList>(
    "https://dummyjson.com/todos",
    { todos: [] }
  );

  return (
    <>
      <h2>ToDos</h2>
      <Card>
        <ListGroup variant="flush">
          {isLoading ? (
            <LabledSpinner />
          ) : (
            data.value.todos.map((t) => (
              <ListGroup.Item key={t.id}>{t.todo}</ListGroup.Item>
            ))
          )}
        </ListGroup>
      </Card>
    </>
  );
};

interface LabledSpinnerProps {
  label?: string;
}
const LabledSpinner = ({ label = "Loading ..." }: LabledSpinnerProps) => {
  return (
    <div class="d-flex justify-content-center mt-4 mb-4">
      <Spinner animation="border" variant="primary">
        {label && <span className="visually-hidden">{label}</span>}
      </Spinner>
      {label && <span class="mt-1 ms-2">{label}</span>}
    </div>
  );
};

export default TodosPage;
