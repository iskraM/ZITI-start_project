import React, { useEffect } from "react";
import ReactDOM from "react-dom";

import "./index.scss";

import SkiCardsContainer from "skicards/SkiCardsContainer";
import Profile from "profile/Profile";

import NavBar from "./components/NavBar";
import Login from "./components/Login";
import Register from "./components/Register";

function App() {
	const [isLoggedIn, setIsLoggedIn] = React.useState(false);

	useEffect(() => {
		// check if session has user data
		// if yes, set isLoggedIn to true
		// else set isLoggedIn to false
		const usename = sessionStorage.getItem("username");
		if (usename != null) {
			setIsLoggedIn(true);
		} else {
			setIsLoggedIn(false);
			component = <Login />;
		}
	});

	let component;
	switch (window.location.pathname) {
		case "/":
			if (isLoggedIn) {
				component = <SkiCardsContainer />;
			} else {
				component = <Login />;
			}
			break;
		case "/profile":
			if (isLoggedIn) {
				component = <Profile />;
			} else {
				component = <Login />;
			}
			break;
		case "/login":
			component = <Login />;
			break;
		case "/register":
			component = <Register />;
			break;
		default:
			if (isLoggedIn) {
				component = <SkiCardsContainer />;
			} else {
				component = <Login />;
			}
			break;
	}

	return (
		<div className="mt-2 text-xl mx-auto max-w-6xl">
			<NavBar isLoggedIn={isLoggedIn} />
			{component}
		</div>
	);
};

ReactDOM.render(<App />, document.getElementById("app"));
