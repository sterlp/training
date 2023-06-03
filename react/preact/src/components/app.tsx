import { h } from "preact";
import { Route, Router } from "preact-router";

import Header from "./header";

// Code-splitting is automated for `routes` directory
import Home from "../routes/home";
import Profile from "../routes/profile";
import { signal } from "@preact/signals";
import TodosPage from "src/routes/todos";
import UseEffectPage from "src/routes/use-effect/use-effect-page";

const activePath = signal("/");

const App = () => (
  <div id="app">
    <Header activeUrl={activePath} />
    <div class="container">
      <Router onChange={(e) => (activePath.value = e.url)}>
        <Route path="/" component={Home} />
        <Route path="/profile/" component={Profile} user="me" />
        <Route path="/todos" component={TodosPage} />
        <Route path="/use-effect" component={UseEffectPage} />
      </Router>
    </div>
  </div>
);

export default App;
