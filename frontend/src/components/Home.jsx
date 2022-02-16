import { useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { Row } from "react-bootstrap";

function Home() {
  const navigate = useNavigate();

  useEffect(() => {
    if (localStorage.jwt) {
      axios({
        method: "get",
        url: "/auth/falsify/",
        headers: { Authorization: localStorage.getItem("jwt") },
      })
        .then((res) => {
          console.log(res);
        })
        .catch((err) => {
          console.log(err);
          // 토큰 만료되면 로그아웃
          localStorage.removeItem("jwt");
          navigate("/");
        });
    }
  }, []);

  return (
    <div>
      <img
        src="../img/home2.png"
        alt=""
        style={{ width: "100%" }}
        className="d-flex mx-auto d-none d-lg-block"
      />
      <img
        src="../img/home3.png"
        alt=""
        style={{ width: "100%" }}
        className="d-flex mx-auto d-none d-sm-block d-lg-none"
      />
      <img
        src="../img/home4.png"
        alt=""
        style={{ width: "100%" }}
        className="d-flex mx-auto d-sm-none"
      />
      <br /> <br />
      <div style={{ width: "90%", maxWidth:"1200px" }} className="mx-auto">
        <Row xs={1} sm={2} md={3} lg={4} className="">
          {Array.from({ length: 11 }).map((_, idx) => (
            <div className="mb-4">
              <div className="">
                <img
                  src="../img/thumbnail.png"
                  alt=""
                  style={{ height: "100%", width: "100%" }}
                />
              </div>
              <div className="d-flex mt-2">
                <img
                  src="../img/user.png"
                  alt=""
                  style={{ width: "50px", height: "50px" }}
                />
                <div className="ms-1">
                  <div className="fw-bold">방송 제목</div>
                  <div>닉네임</div>
                </div>
              </div>
            </div>
          ))}
        </Row>
      </div>
    </div>
  );
}

export default Home;
