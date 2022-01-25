import { Link } from "react-router-dom";
import { useSelector } from "react-redux";

function NavBar() {
  const { isLogin } = useSelector((state) => ({
    isLogin: state.member.isLogin,
  }));
  console.log(isLogin);
  return (
    <div>
      <ul>
        <li>
          <Link to="/">홈</Link>
        </li>
        {!isLogin ? (
          <li>
            <Link to="/login">로그인</Link>
          </li>
        ) : (
          <li>로그아웃</li>
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
