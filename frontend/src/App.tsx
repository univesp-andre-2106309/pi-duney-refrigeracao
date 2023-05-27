import "react-toastify/dist/ReactToastify.css";

import "./App.css";

import { PageContainer } from "./components/containers/PageContainer";
import { Outlet, useNavigate } from "react-router-dom";
import { useLocation } from "react-router-dom";
import { ToastContainer } from "react-toastify";
import { useEffect, useState } from "react";
import { Row } from "react-bootstrap";
import { useLoginRefresh } from "./hooks/useLoginRefresh";
import pageTitles from "./router/routesPageTitles";


function App() {
  const [location, setLocation] = useState<string>("");
  const navigate = useNavigate();
  const [authToken, callLoginStatus] = useLoginRefresh();
  const [pageLoadead, setPageLoadead] = useState<boolean>(false)

  const currentPath = useLocation();

  if (location !== currentPath.pathname) {
    setLocation(currentPath.pathname);
  }

  useEffect(() => {
    if (currentPath.pathname === "/") {
      navigate("/menu");
    }
  }, []);

  useEffect(() => {
    callLoginStatus();
    setPageLoadead(true);
  }, [location]);

  return (
    <div className="App">
      <div className="content-container">
        {location !== "/login" && pageLoadead && (
          <PageContainer
            enableHeader={true}
            title={pageTitles.find((item) => item.name === location)?.title}
          >
            <Outlet />
          </PageContainer>
        )}

        {location.includes("/login") && (
          <PageContainer enableHeader={false}>
            <Outlet />
          </PageContainer>
        )}
        <ToastContainer />
      </div>

      {/* <div className="footer-container">
        <h4>Duney Refrigeração 1.0</h4>
        <p>Projeto integrador univesp</p>
      </div> */}
    </div>
  );
}

export default App;
