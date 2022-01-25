import React, { useState } from "react";
import axios from "axios";
import { useDispatch } from "react-redux";
import { login } from "../../modules/member";
import { useNavigate } from "react-router-dom";
import SetAuth from "./SetAuth";

function Login() {
  const dispatch = useDispatch();
  const loginSuccess = () => dispatch(login());
  const navigate = useNavigate();
  const JWT_EXPIRY_TIME = 24 * 3600 * 1000;
  const [inputs, setInputs] = useState({
    inputEmail: "",
    inputPassword: "",
  });
  const { inputEmail, inputPassword } = inputs;
  const onChange = (e) => {
    const { value, name } = e.target;
    setInputs({
      ...inputs,
      [name]: value,
    });
  };
  const onClick = () => {
    console.log(inputs);
    const data = {
      email: inputEmail,
      password: inputPassword,
    };
    axios
      .post("/user/signin", data)
      .then((response) => {
        const token = response.data;
        localStorage.setItem("jwt", token);
        localStorage.setItem("isLogin", true);
        SetAuth(token);
        loginSuccess();
        navigate("/");
      })
      .catch((error) => {
        console.log(error);
      });
  };

  const onSilentRefresh = () => {};

  return (
    <div>
      <input
        type="email"
        placeholder="이메일 입력"
        name="inputEmail"
        onChange={onChange}
        value={inputEmail}
      />
      <input
        type="password"
        placeholder="비밀번호 입력"
        name="inputPassword"
        onChange={onChange}
        value={inputPassword}
      />
      <button onClick={onClick}>로그인</button>
    </div>
  );
}

export default Login;
