"use client";

import Image from "next/image";
import Button from "react-bootstrap/Button";
import { useState } from "react";

export default function Home() {
  const [count, setCount] = useState(0);
  return <Button onClick={() => setCount(count + 1)}>Home {count}</Button>;
}
