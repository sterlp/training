import { h } from "preact";
import Header from "../src/components/header";
// See: https://github.com/preactjs/enzyme-adapter-preact-pure
import { shallow } from "enzyme";
import { signal } from "@preact/signals";

describe("Initial Test of the Header", () => {
  test("Header renders 3 nav items", () => {
    // GIVEN
    const activeUrl = signal("/todos");
    // WHEN
    const context = shallow(<Header activeUrl={activeUrl} />);
    // THEN
    expect(context.find(".navbar-brand").text()).toContain("PReact");
    expect(context.find("Link").length).toBe(3);
  });
});
