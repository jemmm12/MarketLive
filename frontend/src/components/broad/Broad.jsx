import { useRef } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import axios from "axios";
import { Button } from "react-bootstrap";
import $ from 'jquery'
// import { kurentoUtils } from './kurento-utils.js'
import { WebRtcPeer } from 'kurento-utils';


function Broad() {

  //채팅 메시지 보내기
  const [chatting, setChatting] = useState("");
  const [messages, setMessages] = useState([]);
  const chattingBox = useRef();

  const onChange = (e) => {
    setChatting(e.target.value);
  };

  const onKeyPress = (e) => {
    if (e.key === "Enter" && chatting !== "") {
      sendMessage(chatting);
      e.target.value = "";
      setChatting("");
    }
  };

  const sendMessage = (message) => {
    setMessages(messages.concat(message));
  };

  useEffect(() => {
    chattingBox.current.scrollTop = chattingBox.current.scrollHeight;
    console.log("update");
  }, [messages]);

  const messageList = messages.map((msg) => <div>{msg}</div>);


  //
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
  // const mediaStreamConstraints = {
  //   video: {
  //     width: 1280,
  //     height: 720,
  //   },
  //   audio: true,
  // };

  // const localVideo = useRef();

  // let localStream;

  // const startVideo = async () => {
  //   const stream = await navigator.mediaDevices.getUserMedia(
  //     mediaStreamConstraints
  //   );
  //   if (localVideo && localVideo.current && !localVideo.current.srcObject) {
  //     localVideo.current.srcObject = stream;
  //     localVideo.current.onloadedmetadata = (e) => {
  //       localVideo.current.play();
  //     };
  //   }
  // };

  // console.log(navigator.mediaDevices.enumerateDevices());

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


  // webRTC

  useEffect(() => {
    var myurl = 'i6c110.p.ssafy.io:8113'
    var ws = new WebSocket('wss://' + myurl + '/i6c110');
    // var ws = new WebSocket('https://i6c110.p.ssafy.io:8443/i6c110')
    var video;
    var webRtcPeer;
    // var broad_id;	// 테스트용 broadcaster id
    // var viewer_id;
    var broad_id = 724212
    var viewer_id = 724212
    var nickname = 'asddf'

    console.log('-----')
    presenter()
    // viewer()

    window.onload = function () {
        // console = new Console();
        video = document.getElementById('video');
        // disableStopButton();
        // enableButton('#sendBroadId', 'broadId()');
        // enableButton('#sendViewerId', 'viewerId()');
    }

    // function broadId() {
    //     broad_id = document.getElementById('broadId').value;
    //     console.log(broad_id);
    // }
    // const broadId = () => {
    //   broad_id = document.getElementById('broadId').value;
    //   console.log(broad_id);
    // }

    // function viewerId() {
    //     viewer_id = document.getElementById('viewerId').value;
    //     console.log(viewer_id);
    // }


    function sendMessage(message) {
      var jsonMessage = JSON.stringify(message);
      console.log('Sending message: ' + jsonMessage);
      ws.send(jsonMessage);
    }

    ws.onmessage = function (message) {
        var parsedMessage = JSON.parse(message.data);
        console.info('Received message: ' + message.data);

        switch (parsedMessage.id) {
            case 'presenterResponse':
                presenterResponse(parsedMessage);
                break;
            case 'viewerResponse':
                viewerResponse(parsedMessage);
                break;
            case 'iceCandidate':
                webRtcPeer.addIceCandidate(parsedMessage.candidate, function (error) {
                    if (error)
                        return console.error('Error adding candidate: ' + error);
                });
                break;
            case 'stopCommunication':
                dispose();
                break;
            default:
                console.error('Unrecognized message', parsedMessage);
        }
    }

    window.onbeforeunload = function () {
        ws.close();
    }


    function presenterResponse(message) {
        if (message.response !== 'accepted') {
            var errorMsg = message.message ? message.message : 'Unknow error';
            console.info('Call not accepted for the following reason: ' + errorMsg);
            dispose();
        } else {
            webRtcPeer.processAnswer(message.sdpAnswer, function (error) {
                if (error)
                    return console.error(error);
            });
        }
    }

    function viewerResponse(message) {
        if (message.response !== 'accepted') {
            var errorMsg = message.message ? message.message : 'Unknow error';
            console.info('Call not accepted for the following reason: ' + errorMsg);
            dispose();
        } else {
            webRtcPeer.processAnswer(message.sdpAnswer, function (error) {
                if (error)
                    return console.error(error);
            });
        }
    }

    function presenter() {
        if (!webRtcPeer) {
            // showSpinner(video);

            var options = {
                localVideo: video,
                onicecandidate: onIceCandidate
            }
            webRtcPeer = new WebRtcPeer.WebRtcPeerSendonly(options,
                function (error) {
                    if (error) {
                        return console.error(error);
                    }
                    webRtcPeer.generateOffer(onOfferPresenter);
                });

            // enableStopButton();
        }
    }

    function onOfferPresenter(error, offerSdp) {
        if (error)
            return console.error('Error generating the offer');
        // console.info('Invoking SDP offer callback function ' + location.host);
        var message = {
            id: 'makeRoom',
            roomid: broad_id,
            userid: viewer_id,
            nickname: nickname,
            sdpOffer: offerSdp
        }
        sendMessage(message);
    }

    function viewer() {
        if (!webRtcPeer) {
            // showSpinner(video);

            var options = {
                remoteVideo: video,
                onicecandidate: onIceCandidate
            }
            webRtcPeer = new WebRtcPeer.WebRtcPeerRecvonly(options,
                function (error) {
                    if (error) {
                        return console.error(error);
                    }
                    this.generateOffer(onOfferViewer);
                });

            // enableStopButton();
        }
    }

    function onOfferViewer(error, offerSdp) {
        if (error)
            return console.error('Error generating the offer');
        // console.info('Invoking SDP offer callback function ' + location.host);
        var message = {
            id: 'enterRoom',
            roomid: broad_id,
            userid: viewer_id,
            nickname: nickname,
            sdpOffer: offerSdp
        }
        sendMessage(message);
    }

    function onIceCandidate(candidate) {
        console.log("Local candidate" + JSON.stringify(candidate));

        var message = {
            id: 'onIceCandidate',
            roomid: broad_id,
            userid: viewer_id,
            candidate: candidate
        };
        sendMessage(message);
    }

    function stop() {
        var message = {
            id: 'stop'
        }
        sendMessage(message);
        dispose();
    }

    function dispose() {
        if (webRtcPeer) {
            webRtcPeer.dispose();
            webRtcPeer = null;
        }
        // hideSpinner(video);

        // disableStopButton();
    }

    // function disableStopButton() {
    //     enableButton('#presenter', 'presenter()');
    //     enableButton('#viewer', 'viewer()');
    //     disableButton('#stop');
    // }

    // function enableStopButton() {
    //     disableButton('#presenter');
    //     disableButton('#viewer');
    //     enableButton('#stop', 'stop()');
    // }

    // function disableButton(id) {
    //     $(id).attr('disabled', true);
    //     $(id).removeAttr('onclick');
    // }

    // function enableButton(id, functionName) {
    //     $(id).attr('disabled', false);
    //     $(id).attr('onclick', functionName);
    // }



    // function showSpinner() {
    //     for (var i = 0; i < arguments.length; i++) {
    //         arguments[i].poster = './img/transparent-1px.png';
    //         arguments[i].style.background = 'center transparent url("./img/spinner.gif") no-repeat';
    //     }
    // }

    // function hideSpinner() {
    //     for (var i = 0; i < arguments.length; i++) {
    //         arguments[i].src = '';
    //         arguments[i].poster = './img/webrtc.png';
    //         arguments[i].style.background = '';
    //     }
    // }

    /**
     * Lightbox utility (to display media pipeline image in a modal dialog)
     */
    // $(document).delegate('*[data-toggle="lightbox"]', 'click', function (event) {
    //     event.preventDefault();
    //     $(this).ekkoLightbox();
    // });


  },[])
  



  return (
    <div>
      <div
        style={{ width: "100%", maxWidth: "1400px" }}
        className="mx-auto d-md-flex"
      >
        <div className="w-100 p-0">
          {/* 비디오 */}
          <div id="videoBig">
				  	<video id="video" autoPlay width="640px" height="480px"></video>
				  </div>
          {/* <video
            id="video"
            // poster="../img/thumbnail.png"
            className="w-100"
            // ref={localVideo}
            autoPlay
            // playsInline
          ></video> */}

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
                  // onClick={startVideo}
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
          ref={chattingBox}
          className="p-0 border"
          // style={{ minHeight: "200px", width:"400px"}}
          style={{
            position: "relative",
            minHeight: "400px",
            width: "400px",
            height: "400px",
            overflow: "auto",
          }}

        >
          <div style={{ position: "sticky", top: 0, backgroundColor: "white" }}>
            채팅창
          </div>
          <div>{messageList}</div>
          <input
            placeholder="채팅을 입력해주세요"
            onChange={onChange}
            onKeyPress={onKeyPress}
            style={{
              position: "sticky",
              width: "100%",
              clear: "left",
              float: "left",
              top: "400px",
              left: 0,
              bottom: 0,
            }}
          ></input>
        </div>



        {/* <div
          ref={chattingBox}
          className="p-0 border d-md-none"
          // style={{ minHeight: "200px", width:"400px"}}
          style={{
            position: "relative",
            minHeight: "400px",
            width: "100%",
            height: "400px",
            overflow: "auto",
          }}

        >
          <div style={{ position: "sticky", top: 0, backgroundColor: "white" }}>
            채팅창
          </div>
          <div>{messageList}</div>
          <input
            placeholder="채팅을 입력해주세요"
            onChange={onChange}
            onKeyPress={onKeyPress}
            style={{
              position: "sticky",
              width: "100%",
              clear: "left",
              float: "left",
              top: "400px",
              left: 0,
              bottom: 0,
            }}
          ></input>
        </div> */}
        {/* <div
          className="p-0 border d-md-none"
          style={{ minHeight: "200px", width:"100%", height:"40vh"}}
        >
          채팅창
        </div> */}
      </div>



      {/* webRTC */}
      {/* <input type="text" id="broadId"/><button id="sendBroadId" onClick={broadId}>broad_id 입력</button>
      <input type="text" id="viewerId"/><button id="sendViewerId" onClick={viewerId}>viewer_id 입력</button>
      <a id="presenter" className="btn btn-success" onClick={presenter}>
        <span className="glyphicon glyphicon-play"></span> Presenter </a> 

        <a id="viewer"  className="btn btn-primary" onClick={viewer}>
          <span className="glyphicon glyphicon-user"></span> Viewer</a> 

          <a id="stop" className="btn btn-danger" disabled="disabled">
            <span className="glyphicon glyphicon-stop"></span> Stop</a> */}





    </div>
  );
}

export default Broad;
