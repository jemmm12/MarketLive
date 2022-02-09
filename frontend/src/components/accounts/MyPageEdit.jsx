import { useEffect, useState } from 'react';
import axios from 'axios';
import { Link, useNavigate } from "react-router-dom";
import { Spinner, FormControl, Button } from 'react-bootstrap'


function MyPageEdit() {

    const navigate = useNavigate();

    const [num, setNum] = useState(0)
    const [originNickname, setOriginNickname] = useState('')
    const [nicknameMsg, setNicknameMsg] = useState('')
    const [nicknameMsgColor, setNicknameMsgColor] = useState({ color: "black" })
    const [checkNickname, setCheckNickname] = useState(false)


    // input으로 수정값 받아오기
    const [inputs, setInputs] = useState({
        email: '',
        manner: '',
        name: '',
        nickname: '',
        oneline: '',
        phone: '',
        thumnailroot: '',
        userid: '',
    });
    const onChange = (e) => {
        const { value, name } = e.target;
        setInputs({
            ...inputs, 
            [name]: value 
        });
    };


    // 회원정보 받아오기
    useEffect(() => {
        axios({
            method: 'get',
            url: '/user/mypage/',
            headers: {Authorization: localStorage.getItem('jwt')}
        })
        .then(res => { 
            console.log(res)
            setOriginNickname(res.data.nickname)    // 기존 닉네임 저장
            setInputs(res.data)
        })
        .catch(err => {
            console.log(err)
            // 토큰 만료되면 로그아웃
            localStorage.removeItem("jwt");
            navigate("/login");
        })
    }, [])


    // 닉네임 유효성검사
    useEffect(() => {
        setNum(num+1)
        setCheckNickname(false)
        setNicknameMsg('')
        const debounce = setTimeout(() => {
            if (inputs.nickname.length >= 1){
                if (inputs.nickname === originNickname){
                    if (num > 1){
                        setNicknameMsg('기존 닉네임입니다.')
                    }
                    setNicknameMsgColor({ color: "black" })
                    setCheckNickname(true)
                }
                else if (inputs.nickname.substr(0,1) === ' ' || inputs.nickname.substr(-1,1) === ' '){
                    setNicknameMsg('처음과 끝에 공백을 넣을 수 없습니다.')
                    setNicknameMsgColor({ color: "red" })
                }
                else if (inputs.nickname.includes('  ')){
                    setNicknameMsg('공백을 두번 연속 포함할 수 없습니다.')
                    setNicknameMsgColor({ color: "red" })
                }
                else{   // 닉네임 중복체크
                    axios({
                        method: 'get',
                        url: '/user/checknickname?nickname='+inputs.nickname
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
        }, [inputs.nickname])


    // 회원정보 수정
    const onEdit = () => {
        if (checkNickname){
            axios({
                method: 'post',
                url: '/user/update/',
                headers: {Authorization: localStorage.getItem('jwt')},
                data: {
                    "nickname": inputs.nickname,
                    "oneline": inputs.oneline,
                    "phone": inputs.phone,
                },
            })
                .then(res => {
                    console.log(res)
                    navigate("/mypage");
                })
                .catch(err => {
                    console.log(err)
                    // 토큰 만료되면 로그아웃
                    localStorage.removeItem("jwt");
                    navigate("/login");
                })
        }
        else{
            alert('다시 입력해주세요.')
        }
    }

    const onExit = () => {
        navigate('/mypage')
    }


    if (inputs.email === ''){
        return(
            <div className="d-flex">
            <Spinner animation="border" className="mt-5 mx-auto" />
            </div>
        )
    }

    return(
        <div>
            {/* <h1>MyPageEdit</h1>
            <div>
                이메일: {inputs.email}
            </div>
            <div>
                매너온도: {inputs.manner}
            </div>
            <div>
                이름: {inputs.name}
            </div>
            <div>
                닉네임: 
                <input
                name="nickname"
                value={inputs.nickname}
                onChange={onChange}
                maxLength={15}
                />
                <span style={nicknameMsgColor}>
                    {nicknameMsg}
                </span>
            </div>
            <div>
                소개: 
                <input
                name="oneline"
                value={inputs.oneline}
                onChange={onChange}
                maxLength={50}
                />
            </div>
            <div>
                전화번호: 
                <input
                name="phone"
                value={inputs.phone}
                onChange={onChange}
                maxLength={15}
                />
            </div>
            <button onClick={onEdit}>수정</button>
            <Link to="/mypage">
                <button>취소</button>
            </Link> */}


            <div className='mx-auto mt-4' style={{width:"70%"}}>
                <div className='border-bottom border-3 border-dark'>
                    <h2 className='fw-bold ms-1'>프로필 수정</h2>
                </div>
                <div className='mt-4 d-flex ms-1'>
                    <img src="../img/user.png" alt="" style={{width:"80px"}}/>
                    <h2 className='mt-auto ms-2'> 
                        <span className='fw-bold'>{originNickname}</span> 님
                    </h2>
                    <h5 className='mt-auto ms-auto me-1'>
                        매너온도 {inputs.manner}도
                    </h5>
                </div>
                <hr />
                <div className='ms-1'>
                    <h6>이메일</h6>
                    <h5>{inputs.email}</h5>
                </div>
                <hr />
                <div className='ms-1'>
                    <h6>이름</h6>
                    <h5>{inputs.name}</h5>
                </div>
                <hr />
                <div className='ms-1'>
                    <h6>닉네임</h6>
                    <FormControl
                       name="nickname"
                       value={inputs.nickname}
                       onChange={onChange}
                       maxLength={15}
                    />
                    <div className='ms-1'>
                        <span style={nicknameMsgColor}>
                            {nicknameMsg}
                        </span>
                    </div>
                </div>
                <hr />
                <div className='ms-1'>
                    <h6>전화번호</h6>
                    <FormControl
                        name="phone"
                        value={inputs.phone}
                        onChange={onChange}
                        maxLength={15}
                    />
                </div>
                <hr />
                <div className='ms-1'>
                    <h6>소개글</h6>
                    <FormControl
                        name="oneline"
                        value={inputs.oneline}
                        onChange={onChange}
                        maxLength={50}
                    />
                </div>
                <hr />
                <div className='d-flex' >
                    <Button 
                        className='ms-auto' 
                        variant="secondary"
                        onClick={onEdit}
                    >
                        수정
                    </Button>
                    <Button
                        className='ms-2' 
                        variant="secondary"
                        onClick={onExit}
                    >
                        취소
                    </Button>
                </div>
            </div>
        </div>
    )
}

export default MyPageEdit;