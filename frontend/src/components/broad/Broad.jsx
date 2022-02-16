import { useRef } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import axios from "axios";
import { Button } from "react-bootstrap";

function Broad() {
  const navigate = useNavigate();
  const { nickname } = useParams();

  const [myNickname, setMyNickname] = useState("");

  useEffect(() => {
    axios({
      method: "get",
      url: "/user/mypage/",
      headers: { Authorization: localStorage.getItem("jwt") },
    })
      .then((res) => {
        console.log(res);
        setMyNickname(res.data.nickname);
      })
      .catch((err) => {
        console.log(err);
        // 토큰 만료되면 로그아웃
        localStorage.removeItem("jwt");
        navigate("/login");
      });
  }, []);

  // 방송
  const mediaStreamConstraints = {
    video: {
      width: 1280,
      height: 720,
    },
    audio: true,
  };

  const localVideo = useRef();

  let localStream;

  const startVideo = async () => {
    const stream = await navigator.mediaDevices.getUserMedia(
      mediaStreamConstraints
    );
    if (localVideo && localVideo.current && !localVideo.current.srcObject) {
      localVideo.current.srcObject = stream;
      localVideo.current.onloadedmetadata = (e) => {
        localVideo.current.play();
      };
    }
  };

  console.log(navigator.mediaDevices.enumerateDevices());

  //   function gotLocalMediaStream(mediaStream) {
  //     localStream = mediaStream;
  //     localVideo.current.srcObject = mediaStream;
  //     localVideo.current.onloadedmetadata = (e) => {
  //       localVideo.current.play();
  //     };
  //   }

  //   function handleLocalMediaStreamError(error) {
  //     console.log("navigator.getUserMedia error: ", error);
  //   }

  //   navigator.mediaDevices
  //     .getUserMedia(mediaStreamConstraints)
  //     .then(gotLocalMediaStream)
  //     .catch(handleLocalMediaStreamError);

  const onProfile = () => {
    window.open("/profile/" + nickname, "_blank");
  };

  return (
    <div>
      <div
        style={{ width: "100%", maxWidth: "1400px" }}
        className="mx-auto d-md-flex"
      >
        <div className="w-100 p-0">
          {/* 비디오 */}
          <video
            poster="../img/thumbnail.png"
            className="w-100"
            ref={localVideo}
            autoPlay
            playsInline
          ></video>

          {/* 비디오 밑 */}
          <div className="d-flex mb-2">
            {/* 이미지, 방송제목, 닉네임 */}
            <img
              src="../img/user.png"
              alt=""
              style={{ width: "50px", height: "50px", cursor: "pointer" }}
              className="ms-1"
              onClick={onProfile}
            />
            <div className="ms-1">
              <div className="fw-bold">방송 제목</div>
              <div>
                <span onClick={onProfile} style={{ cursor: "pointer" }}>
                  {nickname}
                </span>
              </div>
            </div>

            {/* 판매자일 경우 보이는 버튼 */}
            {nickname === myNickname ? (
              <div className="ms-auto me-1">
                <Button
                  onClick={startVideo}
                  className="ms-2"
                  variant="secondary"
                  size="sm"
                >
                  화면 송출
                </Button>
                <Button className="ms-2" variant="secondary" size="sm">
                  음소거
                </Button>
                <Button className="ms-2" variant="secondary" size="sm">
                  설정
                </Button>
                <Button className="ms-2" variant="secondary" size="sm">
                  방송 종료
                </Button>
              </div>
            ) : null}
          </div>
        </div>

        {/* 채팅 */}
        <div
          className="d-none d-md-block p-0 border"
          style={{ minHeight: "200px", width:"400px"}}
        >
          채팅창
        </div>
        <div
          className="p-0 border d-md-none"
          style={{ minHeight: "200px", width:"100%", height:"40vh"}}
        >
          채팅창
        </div>
      </div>
    </div>
  );
}

export default Broad;
