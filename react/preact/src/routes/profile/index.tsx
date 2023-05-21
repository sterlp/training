import { effect, signal } from "@preact/signals";
import { h } from "preact";
import { Button, Card } from "react-bootstrap";

interface Props {
  user: string;
}
const count = signal(1);
const time = signal(Date.now());

// Note: `user` comes from the URL, courtesy of our router
const Profile = ({ user }: Props) => {
  effect(() => {
    const timer = setInterval(() => (time.value = Date.now()), 1000);
    return () => clearInterval(timer);
  });

  return (
    <Card>
      <Card.Body>
        <Card.Title>Profile: {user}</Card.Title>
        <Card.Text>
          This is the user profile for a user named {user}.
          <p>Current time: {new Date(time.value).toLocaleString()}</p>
          <p>Clicked {count} times.</p>
        </Card.Text>
        <Button
          variant="primary"
          onClick={() => (count.value = count.value + 1)}
        >
          Click Me
        </Button>
      </Card.Body>
    </Card>
  );
};

export default Profile;
