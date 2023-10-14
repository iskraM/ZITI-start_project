import React from "react";
import ReactDOM from "react-dom";
import "./index.scss";

import SkiCardsContainer from "./components/SkiCardsContainer";

const App = () => (
  <div className="mt-10 text-3xl mx-auto max-w-6xl">
    <SkiCardsContainer />
  </div>
);
ReactDOM.render(<App />, document.getElementById("app"));
