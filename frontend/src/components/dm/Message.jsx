import { Link } from 'react-router-dom';
import { useEffect, useState } from 'react';
import axios from 'axios';
import MessageSideBar from './MessageSideBar';
import { Spinner, Table } from 'react-bootstrap'

function Message() {

    const [myId, setMyId] = useState('')
    const [msgs, setMsgs] = useState('')


    // 나의 pk 저장
    useEffect(() => {
        axios({
            method: 'get',
            url: '/user/mypage/',
            headers: {Authorization: localStorage.getItem('jwt')}
        })
        .then(res => { 
            // console.log(res)
            setMyId(res.data.userid)
        })
        .catch(err => {
            console.log(err)
        })
    }, [])


    // 받은 쪽지 불러오기
    useEffect(() => {
        if (myId !==''){
            axios({
                method: 'get',
                url: '/dm/all/'+myId
            })
            .then(res => {
                console.log(res.data)
                setMsgs(res.data.reverse())
            })
            .catch(err => {
                console.log(err)
            })
        }
    }, [myId])


    // axios에서 데이터를 받아오기 전에 key값 오류 방지
    if (msgs === ''){
        return(
            <div className="d-flex">
                <MessageSideBar></MessageSideBar>
                <Spinner animation="border" className="mt-5 mx-auto" />
            </div>
        )
    }


    return(
        <div className="d-flex">
            <MessageSideBar></MessageSideBar>

            {/* <table style={{border: '1px solid #444444', width: '70%'}}>
                <thead>
                    <tr>
                    <th>제목</th><th>보낸이</th><th>날짜</th>
                    </tr>
                </thead>
                <tbody>
                    {msgs.map((msg) => (
                        <tr key={msg.dm_id}>
                            <td>
                                <Link to={`/message/read/${msg.dm_id}`}>
                                    {msg.dm_title}
                                </Link>
                            </td>
                            <td>
                                <Link to={`/profile/${msg.senderId.user_nickname}`}>
                                    {msg.senderId.user_nickname}
                                </Link> 
                            </td>
                            <td>{msg.dm_time}</td>
                        </tr>
                    ))}
                </tbody>
            </table> */}


            <Table style={{ width: '70%' }} className="mx-auto mt-5">
                <thead>
                    <tr>
                    <th style={{ width: '40%' }}>제목</th>
                    <th style={{ width: '20%' }}>보낸이</th>
                    <th style={{ width: '10%' }}>날짜</th>
                    </tr>
                </thead>
                <tbody>
                    {msgs.map((msg) => (
                        <tr key={msg.dm_id}>
                            <td>
                                <Link 
                                    className='text-dark'
                                    style={{ textDecoration: 'none' }}
                                    to={`/message/read/${msg.dm_id}`}
                                >
                                    {msg.dm_title}
                                </Link>
                            </td>
                            <td>
                                <Link 
                                    className='text-dark'
                                    style={{ textDecoration: 'none' }}
                                    to={`/profile/${msg.senderId.user_nickname}`}>
                                    {msg.senderId.user_nickname}
                                </Link> 
                            </td>
                            <td>{msg.dm_time}</td>
                        </tr>
                    ))}
                </tbody>
            </Table>
            <div className="ms-auto"></div>

        </div>
    )
}

export default Message;