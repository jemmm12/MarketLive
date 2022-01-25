import { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from "react-router-dom";

function Signup() {
    const URL = ''  // proxy를 이용하면 url은 자동 적용
    const navigate = useNavigate();

    // 유효성 체크에 관한 메시지, 컬러
    const [emailMsg, setEmailMsg] = useState('')
    const [emailMsgColor, setEmailMsgColor] = useState({ color: "black" })
    const [pwMsg, setPwMsg] = useState('')
    const [pwMsgColor, setPwMsgColor] = useState({ color: "black" })
    const [pwCheckMsg, setPwCheckMsg] = useState('')
    const [pwCheckMsgColor, setPwCheckMsgColor] = useState({ color: "black" })
    const [nicknameMsg, setNicknameMsg] = useState('')
    const [nicknameMsgColor, setNicknameMsgColor] = useState({ color: "black" })


    // 회원가입 요청시 확인사항
    const [checkEmail, setCheckEmail] = useState(false)
    const [checkPw, setCheckPw] = useState(false)
    const [checkNinkname, setCheckNickname] = useState(false)


    // input에서 회원정보 받아서 inputs에 저장
    const [inputs, setInputs] = useState({
        email: '',
        password: '',
        passwordConfirmation: '',
        nickname: '',
        name: '',
        phone: '',
    });
    const { email, password, passwordConfirmation, nickname, name, phone } = inputs; 
    const onChange = (e) => {
        const { value, name } = e.target;
        setInputs({
            ...inputs, 
            [name]: value 
        });
    };


     // 이메일에서 작성 후 700ms가 지난 후 실행
     useEffect(() => {
        setCheckEmail(false)
        setEmailMsg('')
        const emailRegex = /([\w-.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/
        const debounce = setTimeout(() => {
            if (email.length >= 1){
                if (!emailRegex.test(email)){
                    setEmailMsg('올바른 이메일 형식이 아닙니다.')
                    setEmailMsgColor({ color: "red" })
                }
                else{   // 이메일 중복검사
                    axios({
                        method: 'get',
                        url: URL+'user/checkemail?email='+email
                    })
                    .then(res => {
                        setEmailMsg('사용하셔도 좋습니다.')
                        setEmailMsgColor({ color: "blue" })
                        setCheckEmail(true)
                    })
                    .catch(err => {
                        setEmailMsg('중복된 이메일입니다.')
                        setEmailMsgColor({ color: "red" })
                    })
                }
            }
        }, 700);
        return () => clearTimeout(debounce);
      }, [email]);


    // 비밀번호, 비밀번호 확인이 변경시 실행
    // 비밀번호가 유효하지 않거나, 비밀번호와 비밀번호 확인이 다를 경우 메시지 출력
    useEffect(() => {
        setCheckPw(false)   // false로 초기화
        if (password.length === 0){
            setPwMsg('')
            setPwCheckMsg('')
        }
        else if (password.length < 6){
            setPwMsg('6자리 이상 입력해주세요.')
            setPwMsgColor({ color: "red" })
            setPwCheckMsg('')
        }
        else{
            setPwMsg('사용하셔도 좋습니다.')
            setPwMsgColor({ color: "blue" })
            if (passwordConfirmation.length === 0){
                setPwCheckMsg('')
            }
            else if(password === passwordConfirmation){
                setPwCheckMsg('비밀번호가 일치합니다.')
                setPwCheckMsgColor({ color: "blue" })
                setCheckPw(true)    // 유효성, 일치여부 확인 후 true
            }
            else{
                setPwCheckMsg('비밀번호가 일치하지 않습니다.')
                setPwCheckMsgColor({ color: "red" })
            }
        }
    }, [password, passwordConfirmation])


   // 닉네임 작성시 실행
   useEffect(() => {
        setCheckNickname(false)
        setNicknameMsg('')
        const debounce = setTimeout(() => {
            if (nickname.length >= 1){
                if (nickname.substr(0,1) === ' ' || nickname.substr(-1,1) === ' '){
                    setNicknameMsg('처음과 끝에 공백을 넣을 수 없습니다.')
                    setNicknameMsgColor({ color: "red" })
                }
                else if (nickname.includes('  ')){
                    setNicknameMsg('공백을 두번 연속 포함할 수 없습니다.')
                    setNicknameMsgColor({ color: "red" })
                }
                else{   // 닉네임 중복체크
                    axios({
                        method: 'get',
                        url: URL+'user/checknickname?nickname='+nickname
                    })
                    .then(res => {
                        setCheckNickname(true)
                        setNicknameMsg('사용하셔도 좋습니다.')
                        setNicknameMsgColor({ color: "blue" })
                    })
                    .catch(err => {
                        setNicknameMsg('중복된 닉네임입니다.')
                        setNicknameMsgColor({ color: "red" })
                    })
                    
                }
            }
        }, 700)
            return() => clearTimeout(debounce)
        }, [nickname])


    // 회원가입 버튼 누를 시 실행
    const onSignup = () => {
        if (checkEmail && checkPw && checkNinkname){
            axios({
                method: 'post',
                url: URL+'user/signup/',
                data: inputs,
            })
                .then(res => {
                    console.log(res)
                    navigate("/login");
                })
                .catch(err => {
                    console.log(err)
                })
        }
        else{
            alert('다시 입력해주세요.')
        }
    }

    return(
        <div>
            <h1>Signup</h1>
            <div>
                <input name="email" 
                placeholder="이메일" 
                onChange={onChange} 
                value={email}
                maxLength={30}
                />
                <span style={emailMsgColor}>
                    {emailMsg}
                </span>
            </div>
            <div>
                <input type="password" 
                name="password" 
                placeholder="비밀번호" 
                onChange={onChange} 
                value={password}
                maxLength={15}
                /> 
                <span style={pwMsgColor}>
                    {pwMsg}
                </span>
            </div>
            <div>
                <input type="password" 
                name="passwordConfirmation" 
                placeholder="비밀번호 확인" 
                onChange={onChange} 
                value={passwordConfirmation} 
                maxLength={15}
                />
                <span style={pwCheckMsgColor}>
                    {pwCheckMsg}
                </span>
            </div>
            <div>
                <input name="nickname" 
                placeholder="닉네임" 
                onChange={onChange} 
                value={nickname}
                maxLength={15}
                />
                <span style={nicknameMsgColor}>
                    {nicknameMsg}
                </span>
            
            </div>
            <div>
                <input name="name" 
                placeholder="이름" 
                onChange={onChange} 
                value={name} 
                maxLength={15}
                />
            </div>
            <div>
                <input name="phone" 
                placeholder="전화번호" 
                onChange={onChange} 
                value={phone} 
                maxLength={15}
                />
            </div>

            <button onClick={onSignup}>회원가입</button>
        </div>
    )
}

export default Signup;