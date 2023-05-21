"use client";

import Image from "next/image";
import Button from "react-bootstrap/Button";

import * as React from "react";

export interface IPersonPageProps {
  count: number;
}

export default function PersonPage() {
  const [count, setCount] = React.useState(0);
  return <Button onClick={() => setCount(count + 1)}>Person {count}</Button>;
}
