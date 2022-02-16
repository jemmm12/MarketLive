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
import BroadMake from "./components/broad/BroadMake";
import Broad from "./components/broad/Broad"
import "./App.css";

import { useState } from "react/cjs/react.development";

function App() {
  return (
    <div>
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
          <Route path="/messageto/:to_nickname" element={<MessageTo />} />
          <Route path="/messageread/:dm_id" element={<MessageRead />} />
          <Route path="/profile/:nickname" element={<Profile />} />
          <Route path="/makebroad" element={<BroadMake />} />
          <Route path="/watch/:nickname" element={<Broad />} />
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
