import { useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";
import { useDispatch } from "react-redux";
import { logout } from "../modules/member";
import {
  Navbar,
  Nav,
  Form,
  Button,
  FormControl,
  Container,
} from "react-bootstrap";

function NavBar() {
  const navigate = useNavigate();

  const { name } = useSelector((state) => ({
    name: state.member.name,
  }));

  const dispatch = useDispatch();
  const logoutSuccess = () => dispatch(logout());
  const onClickLogout = () => {
    localStorage.removeItem("jwt");
    localStorage.removeItem("isLogin");
    logoutSuccess();
    navigate("/");
  };

  // 버튼 누를 때 링크 이동
  const onSignup = () => {
    navigate("/signup");
  };
  const onLogin = () => {
    navigate("/login");
  };
  const onHome = () => {
    navigate("/");
  };
  const onMessage = () => {
    navigate("/message");
  };
  const onMypage = () => {
    navigate("/mypage");
  };

  return (
    <div>
      {/* {name ? <h3>{name}님 안녕하세요</h3> : <h3>로그인이 필요합니다</h3>}
      <ul>
        <li>
          <Link to="/">홈</Link>
        </li>
        {!localStorage.isLogin ? (
          <li>
            <Link to="/login">로그인</Link>
          </li>
        ) : (
          <li onClick={onClickLogout}>로그아웃</li>
        )}
        <li>
          <Link to="/signup">회원가입</Link>
        </li>
        <li>
          <Link to="/mypage">마이페이지</Link>
        </li>
        <li>
          <Link to="/message">쪽지함</Link>
        </li>
      </ul> */}

      <Navbar bg="" expand="md" className="border-bottom border-2">
        <Container fluid>
          <Navbar.Brand
            onClick={onHome}
            style={{ cursor: "pointer" }}
            className="fw-bold ms-4"
          >
            <img src="../img/logo.png" alt="" 
              style={{width:"120px"}}
            />
          </Navbar.Brand>
          <Navbar.Toggle aria-controls="navbarScroll" className="me-3"/>
          <Navbar.Collapse id="navbarScroll">
            <Form className="d-flex ms-auto me-3">
              <FormControl
                type="search"
                placeholder="Search"
                className="me-2 mt-2 my-md-0"
                aria-label="Search"
                // style={{ width: '300px' }}
              />
              <Button variant="outline-secondary" className="mt-2 my-md-0 ">
                Search
              </Button>
            </Form>
            {localStorage.isLogin ? (
              // 로그인 되어 있으면
              <Nav
                className="me-2 my-2 my-md-0 fw-bold"
                style={{ maxHeight: "150px" }}
                navbarScroll
              >
                <Nav.Link onClick={onMessage}>쪽지함</Nav.Link>
                <Nav.Link onClick={onMypage}>마이페이지</Nav.Link>
                <Nav.Link onClick={onClickLogout}>로그아웃</Nav.Link>
              </Nav>
            ) : (
              // 로그인 안되어 있으면
              <Nav
                className="me-2 my-2 my-md-0 fw-bold"
                style={{ maxHeight: "150px" }}
                navbarScroll
              >
                <Nav.Link onClick={onLogin}>로그인</Nav.Link>
                <Nav.Link onClick={onSignup}>회원가입</Nav.Link>
              </Nav>
            )}
          </Navbar.Collapse>
        </Container>
      </Navbar>
    </div>
  );
}

export default NavBar;
