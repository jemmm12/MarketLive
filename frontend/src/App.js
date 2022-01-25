import { BrowserRouter, Route, Routes } from "react-router-dom";
import NavBar from "./components/NavBar";
import Home from "./components/Home";
import Signup from "./components/accounts/Signup";
import Login from "./components/accounts/Login";
import MyPage from "./components/accounts/MyPage";
import Message from "./components/accounts/Message";
import MessageWrite from "./components/accounts/MessageWrite";

import { useState } from "react/cjs/react.development";

function App() {
  return (
    <BrowserRouter>
      <NavBar />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/signup" element={<Signup />} />
        <Route path="/login" element={<Login />} />
        <Route path="/mypage" element={<MyPage />} />
        <Route path="/message" element={<Message />} />
        <Route path="/message/write" element={<MessageWrite />} />
        <Route path="/message/write/:to_nickname" element={<MessageWrite />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
