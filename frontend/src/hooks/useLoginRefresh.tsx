import {useState } from "react";

import { useLocation, useNavigate } from "react-router-dom";

type AuthToken = {
  jwtToken: string;
  refreshToken: string;
};

export const useLoginRefresh = (): [AuthToken, () => void] => {
  const currentPath = useLocation();
  const [authToken, setAuthToken] = useState<AuthToken>({
    jwtToken: "",
    refreshToken: "",
  });
  const navigate = useNavigate();
  

  const callLoginStatus = () => {
    let token = localStorage.getItem("jwtToken")
      ? localStorage.getItem("jwtToken")
      : "";
    let refreshToken = localStorage.getItem("refreshToken")
      ? localStorage.getItem("refreshToken")
      : "";

    const body: AuthToken = {
      jwtToken: token!,
      refreshToken: refreshToken!,
    };

    if (currentPath.pathname !== "/login") {
       
      fetch("http://localhost:8080/api/account/validate", {
        headers: {
          Authorization: token!,
          RefreshToken: refreshToken!,
          "Content-Type": "application/json",
        },
        method: "POST",
        body: JSON.stringify(body),
      })
        .then(async (response) => {
           
          if (!response.ok) {
            navigate("/login");
          }
          const jwtToken = response.headers.get("Authorization");
          const refreshToken = response.headers.get("RefreshToken");

          if (jwtToken && refreshToken) {
            console.log("São diferentes")
            setAuthToken({
                jwtToken,
                refreshToken
            })
            // Save the tokens
            localStorage.setItem("jwtToken", jwtToken!);
            localStorage.setItem("refreshToken", refreshToken!);
          }
        })
        .catch((er) => {
            console.log(er.message)
          navigate("/login");
        });
    }
  };

  return [authToken, callLoginStatus];
};
