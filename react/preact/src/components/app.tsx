import { h } from "preact";
import { Route, Router } from "preact-router";

import Header from "./header";

// Code-splitting is automated for `routes` directory
import Home from "../routes/home";
import Profile from "../routes/profile";
import { signal } from "@preact/signals";

const activePath = signal("/");

const App = () => (
  <div id="app">
    <Header activeUrl={activePath} />
    <div class="container">
      <Router onChange={(e) => (activePath.value = e.url)}>
        <Route path="/" component={Home} />
        <Route path="/profile/" component={Profile} user="me" />
        <Route path="/profile/:user" component={Profile} />
      </Router>
    </div>
  </div>
);

export default App;
