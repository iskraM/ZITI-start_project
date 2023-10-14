import React from 'react';

interface NavbarProps {
  isLoggedIn: boolean;
}

const Navbar: React.FC<NavbarProps> = ({ isLoggedIn }) => {
  const onClickLogout = () => {
    sessionStorage.removeItem("username");
    window.location.href = "/login";
  };

  return (
    <>
      {
        isLoggedIn ? (
          <nav className="flex flex-wrap bg-blue-900 p-6">
            <div className="flex items-center flex-shrink-0 text-white mr-6">
              <a href="/" className="font-semibold text-xl tracking-tight">SkiResort</a>
            </div>
            <div className="block lg:hidden">
              <button className="flex items-center px-3 py-2 border rounded text-gray-500 border-gray-600 hover:text-white hover:border-white">
                <svg className="fill-current h-3 w-3" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><title>Menu</title><path d="M0 3h20v2H0V3zm0 6h20v2H0V9zm0 6h20v2H0v-2z" /></svg>
              </button>
            </div>
            <div className="w-full block flex-grow lg:flex lg:items-center lg:w-auto">
              <div className="text-sm lg:flex-grow">
                <a href="/profile" className="text-lg block mt-4 lg:inline-block lg:mt-0 text-white hover:text-gray-400 mr-4">
                  Profile
                </a>
              </div>
            </div>
            <div className="w-full block flex-grow lg:flex text-right lg:w-auto">
              <div className="text-sm lg:flex-grow">
                <a onClick={onClickLogout} className="text-lg block mt-4 lg:inline-block lg:mt-0 text-white hover:text-gray-400 mr-4">
                  Logout
                </a>
              </div>
            </div>
          </nav>
        ) : (
          <nav className="flex flex-wrap bg-blue-900 p-6">
            <div className="flex items-center flex-shrink-0 text-white mr-6">
              <a className="font-semibold text-xl tracking-tight">SkiResort</a>
            </div>
          </nav>
        )
      }
    </>
  );
};

export default Navbar;