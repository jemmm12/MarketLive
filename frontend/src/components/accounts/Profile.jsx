import { useParams, useNavigate } from 'react-router-dom';
import { useEffect, useState } from 'react'
import axios from 'axios'
import { Spinner, Button } from 'react-bootstrap'



function Profile() {
    const navigate = useNavigate();
    
    // 동적 라우팅 저장
    const {nickname} = useParams()


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

    // 프로필 받아오기
    useEffect(() => {
      axios({
          method: 'get',
          url: '/user/search?nickname='+nickname,
      })
      .then(res => { 
          console.log(res)
          setInputs(res.data)
      })
      .catch(err => {
          console.log(err)
      })
  }, [])


    const onMessageWrite = () => {
      navigate(`/messageto/${nickname}`)
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
          {/* <div>
            {nickname} 님의 프로필
          </div>
          <div>
            이메일: {inputs.email}
          </div>
          <div>
            소개: {inputs.oneline}
          </div>
          <Link to={`/message/to/${nickname}`}>
            <button>
              쪽지 보내기
            </button>
          </Link> */}

          <div className='mx-auto mt-4' style={{width:"70%"}}>
                <div className='border-bottom border-3 border-dark'>
                    <h2 className='fw-bold ms-1'>프로필</h2>
                </div>
                <div className='mt-4 d-flex ms-1'>
                    <img src="../img/user.png" alt="" style={{width:"80px"}}/>
                    <h2 className='mt-auto ms-2'> 
                        <span className='fw-bold'>{inputs.nickname}</span> 님
                    </h2>
                    <h5 className='mt-auto ms-auto me-1'>
                        매너온도 {inputs.manner}0도
                    </h5>
                </div>
                <hr />
                <div className='ms-1'>
                    <h5>{inputs.oneline}</h5>
                </div>
                <hr />
                <div className='ms-1'>
                    <h6>이메일</h6>
                    <h5>{inputs.email}</h5>
                </div>
                <hr />
                <Button 
                    className='d-flex ms-auto' 
                    variant="secondary"
                    onClick={onMessageWrite}
                >
                    쪽지 보내기
                </Button>

            </div>

        </div>
    )
}

export default Profile;