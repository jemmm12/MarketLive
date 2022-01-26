import React, { useState } from "react";
import axios from "axios";
import { useDispatch } from "react-redux";
import { login } from "../../modules/member";
import { useNavigate } from "react-router-dom";
import SetAuth from "./SetAuth";
import jwt_decode from "jwt-decode";

function Login() {
  const dispatch = useDispatch();
  const loginSuccess = () => dispatch(login());
  const navigate = useNavigate();
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
  const onKeyPress = (e) => {
    if (e.key === "Enter") {
      onClick();
    }
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
        const decoded = jwt_decode(token);
        console.log(decoded);
        loginSuccess();
        navigate("/");
      })
      .catch((error) => {
        console.log(error);
      });
  };

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
        onKeyPress={onKeyPress}
      />
      <button onClick={onClick}>로그인</button>
    </div>
  );
}

export default Login;
