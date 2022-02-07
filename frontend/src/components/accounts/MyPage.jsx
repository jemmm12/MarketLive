import { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate } from "react-router-dom";
import { Spinner, Button } from 'react-bootstrap'

function MyPage() {
    const navigate = useNavigate();

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

    useEffect(() => {
        axios({
            method: 'get',
            url: '/user/mypage/',
            headers: {Authorization: localStorage.getItem('jwt')}
        })
        .then(res => { 
            console.log(res)
            setInputs(res.data)
        })
        .catch(err => {
            console.log(err)
        })
    }, [])

    
    const onMyPageEdit = () => {
        navigate('/mypageedit')
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
            {/* <h1>MyPage</h1>
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
                닉네임: {inputs.nickname}
            </div>
            <div>
                소개: {inputs.oneline}
            </div>
            <div>
                전화번호: {inputs.phone}
            </div>
            <Link to="/mypageedit"> 
                <button>수정</button>
            </Link> */}

            <div className='mx-auto mt-4' style={{width:"70%"}}>
                <div className='border-bottom border-3 border-dark'>
                    <h2 className='fw-bold ms-1'>마이페이지</h2>
                </div>
                <div className='mt-4 d-flex ms-1'>
                    <img src="../img/user.png" alt="" style={{width:"80px"}}/>
                    <h2 className='mt-auto ms-2'> 
                        <span className='fw-bold'>{inputs.nickname}</span> 님
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
                    <h5>{inputs.nickname}</h5>
                </div>
                <hr />
                <div className='ms-1'>
                    <h6>전화번호</h6>
                    <h5>{inputs.phone}</h5>
                </div>
                <hr />
                <div className='ms-1'>
                    <h6>소개글</h6>
                    <h5>{inputs.oneline}</h5>
                </div>
                <hr />
                <Button 
                    className='d-flex ms-auto' 
                    variant="light"
                    onClick={onMyPageEdit}
                >
                    수정
                </Button>
            </div>
        </div>
    )
}

export default MyPage;