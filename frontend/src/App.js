import { BrowserRouter, Route, Routes } from "react-router-dom";
import NavBar from "./components/NavBar";
import Home from "./components/Home";
import Signup from "./components/accounts/Signup";
import Login from "./components/accounts/Login";
import MyPage from "./components/accounts/MyPage";
import MyPageEdit from "./components/accounts/MyPageEdit";
import Profile from "./components/accounts/Profile";
import Message from "./components/dm/Message";
import MessageWrite from "./components/dm/MessageWrite";
import MessageTo from "./components/dm/MessageTo";
import MessageRead from "./components/dm/MessageRead";

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
        <Route path="/mypageedit" element={<MyPageEdit />} />
        <Route path="/message" element={<Message />} />
        <Route path="/message/write" element={<MessageWrite />} />
        <Route path="/message/to/:to_nickname" element={<MessageTo />} />
        <Route path="/message/read/:dm_id" element={<MessageRead />} />
        <Route path="/profile/:nickname" element={<Profile />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
