import { h, Fragment } from "preact";
import { Alert, Card, ListGroup, Spinner } from "react-bootstrap";
import { useFetch } from "src/http/http.hooks";
import { ToDosList } from "./todos.model";

const TodosPage = ({}) => {
  const [data] = useFetch<ToDosList>("https://dummyjson.com/todos", {
    todos: [],
  });

  return (
    <>
      <h2>ToDos</h2>
      <Card>
        {data.hasError ? (
          <Alert variant="warning">{JSON.stringify(data.error)}</Alert>
        ) : undefined}
        <ListGroup variant="flush">
          {data.isLoading ? (
            <LabledSpinner />
          ) : (
            data.result.todos.map((t) => (
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
const LabledSpinner = ({ label = "Loading ..." }: LabledSpinnerProps) => (
  <div class="d-flex justify-content-center mt-4 mb-4">
    <Spinner animation="border" variant="primary">
      {label && <span className="visually-hidden">{label}</span>}
    </Spinner>
    {label && <span class="mt-1 ms-2">{label}</span>}
  </div>
);

export default TodosPage;
