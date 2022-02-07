import React, { useState } from "react";
import axios from "axios";
import { useDispatch } from "react-redux";
import { login, setUser } from "../../modules/member";
import { useNavigate } from "react-router-dom";
import SetAuth from "./SetAuth";
import jwt_decode from "jwt-decode";
import { Form, Button, Col, Row, Container } from "react-bootstrap";

function Login() {
  const dispatch = useDispatch();
  const loginSuccess = () => dispatch(login());
  let data = {
    name: "",
    nickname: "",
    email: "",
    phone: "",
  };
  const onSetUser = () => dispatch(setUser(data));
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
        setUserInfo();
        navigate("/");
      })
      .catch((error) => {
        console.log(error);
      });
  };

  const setUserInfo = () => {
    axios({
      method: "get",
      url: "user/mypage/",
      headers: { Authorization: localStorage.getItem("jwt") },
    })
      .then((response) => {
        console.log(response);
        data = {
          name: response.data.name,
          nickname: response.data.nickname,
          email: response.data.email,
          phone: response.data.phone,
        };
        onSetUser(data);
      })
      .catch((error) => {
        console.log(error);
      });
  };

  return (
    // <div>
    //   <input
    //     type="email"
    //     placeholder="이메일 입력"
    //     name="inputEmail"
    //     onChange={onChange}
    //     value={inputEmail}
    //   />
    //   <input
    //     type="password"
    //     placeholder="비밀번호 입력"
    //     name="inputPassword"
    //     onChange={onChange}
    //     value={inputPassword}
    //     onKeyPress={onKeyPress}
    //   />
    //   <button onClick={onClick}>로그인</button>
    // </div>
    <Container fluid="sm">
      <Form>
        <Form.Group as={Row} className="mt-5 justify-content-center">
          <Col sm={4}>
            <Form.Control
              className="mb-1"
              type="email"
              placeholder="이메일 입력"
              name="inputEmail"
              onChange={onChange}
              value={inputEmail}
            ></Form.Control>
            <Form.Control
              className="mb-1"
              type="password"
              placeholder="비밀번호 입력"
              name="inputPassword"
              onChange={onChange}
              value={inputPassword}
              onKeyPress={onKeyPress}
            ></Form.Control>
            <div className="d-grid gap-2">
              <Button onClick={onClick}>로그인</Button>
            </div>
          </Col>
        </Form.Group>
      </Form>
    </Container>
  );
}

export default Login;
