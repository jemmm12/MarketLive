import { Link, useParams, useNavigate } from 'react-router-dom';
import { useState, useEffect } from 'react';
import axios from 'axios';
import MessageSideBar from './MessageSideBar';
import { Button } from 'react-bootstrap'


function MessageRead() {
    const navigate = useNavigate();

    // 동적 라우팅 저장
    const {dm_id} = useParams()

    const [msg, setMsg] = useState('')


    useEffect(() => {
        axios({
            method: 'get',
            url: '/dm/'+dm_id,
        })
        .then(res => {
            console.log(res)
            setMsg(res.data)
        })
        .catch(err => {
            console.log(err)
        })
    },[])


    // axios 받아오기 전에 key값 오류 방지
    if (msg === ''){
        return(
            <MessageSideBar></MessageSideBar>
        )
    }

    const onMessage = () => {
        navigate(`/message/to/${msg.dm.senderId.user_nickname}`)
    }


    return(
        <div className='d-flex'>
            <MessageSideBar></MessageSideBar>
            <div style={{width:"70%"}} className='mx-auto mt-5'>
                <div className=' border p-2'>
                    <div className=''>
                        {msg.dm.dm_title}
                    </div>
                    <hr className='my-2' />

                    <pre style={{whiteSpace: "pre-wrap"}}>{msg.dm.dm_msg}</pre>
                    
                    <span>보낸이: </span>
                    <Link 
                        to={`/profile/${msg.dm.senderId.user_nickname}`}
                        className='text-dark'
                        style={{ textDecoration: 'none' }}
                    >
                        {msg.dm.senderId.user_nickname} 
                    </Link>
                    <br />
                    <span>날짜: </span>
                    {msg.dm.dm_time}
                </div>
                <Button 
                    variant="light"
                    onClick={onMessage} 
                    className='d-flex ms-auto mt-2'
                >
                    답장
                </Button>
            </div>
            <div className="ms-auto"></div>
            
        </div>
    )
}

export default MessageRead;