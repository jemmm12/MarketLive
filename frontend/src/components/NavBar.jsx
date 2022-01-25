import { Link } from "react-router-dom";
import { useSelector } from "react-redux";
import { useDispatch } from "react-redux";
import { logout } from "../modules/member";

function NavBar() {
  const { isLogin } = useSelector((state) => ({
    isLogin: state.member.isLogin,
  }));

  const dispatch = useDispatch();
  const logoutSuccess = () => dispatch(logout());
  const onClickLogout = () => {
    localStorage.removeItem("jwt");
    localStorage.removeItem("isLogin");
    logoutSuccess();
  };

  return (
    <div>
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
      </ul>
    </div>
  );
}

export default NavBar;
