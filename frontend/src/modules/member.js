const SET_USER = "member/SET_USER";
const LOGIN = "member/LOGIN";

export const setUser = () => ({ type: SET_USER });
export const login = () => ({ type: LOGIN });

const initialState = {
  isLogin: false,
  name: "",
  email: "",
  phone: "",
  nickname: "",
};

export default function member(state = initialState, action) {
  switch (action.type) {
    case SET_USER:
      return {
        ...state,
      };
    case LOGIN:
      return {
        ...state,
        isLogin: true,
        email: state.email,
      };
    default:
      return state;
  }
}
