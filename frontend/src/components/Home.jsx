import { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { Row, Spinner } from "react-bootstrap";


function Home() {
  const navigate = useNavigate();

  const [broads, setBroads] = useState('')

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
    axios({
      method: 'get',
      url: '/room/all',
    })
    .then(res => {
      console.log(res)
      setBroads(res.data)
    })
    .catch(err => {
      console.log(err)
    })
  }, []);


  if (broads === ''){
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
        <br /> <br /> <br /> <br />
        <Spinner animation="border" className="d-flex mx-auto" />
      </div>
    )
  }

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
          {broads.map((broad) => (
            <div key={broad.userid}>
              <div className="mb-4">
                <div className="">
                  {broad.thumbnail.includes('.') ? (
                    <img
                      src={broad.thumbnail}
                      alt=""
                      style={{ height: "100%", width: "100%" }}
                    />
                  ) : (
                    <div  style={{ width: "100%"}}>
                    {/* <div style={{ height: "180px", width: "320px" }}> */}
                    <img
                      src='../img/thumbnail.png'
                      // src='../img/home2.png'
                      alt=""
                      style={{ height: "100%", width: "100%" }}
                      // style={{ height: "180px", width: "320px" }}
                    />
                    </div>
                  )}
                </div>
                <div className="d-flex mt-2">
                  <img
                    src="../img/user.png"
                    alt=""
                    style={{ width: "50px", height: "50px" }}
                  />
                  <div className="ms-1">
                    <div className="fw-bold">{broad.title}</div>
                    <div>{broad.nickname}</div>
                  </div>
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
