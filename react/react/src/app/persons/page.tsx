"use client";

import Image from "next/image";
import Button from "react-bootstrap/Button";

import * as React from "react";

export interface IPersonPageProps {
  count: number;
}

export default class PersonPage extends React.Component<
  IPersonPageProps,
  { count: number }
> {
  constructor(props: IPersonPageProps) {
    super(props);
    this.state = { count: 0 };
  }
  handleClick() {
    this.setState({ count: 1 + this.state.count });
  }
  public render() {
    return (
      <Button onClick={this.handleClick}>Person {this.state.count}</Button>
    );
  }
}
