import { useEffect } from 'react';
import axios from 'axios';

function MyPage() {

    useEffect(() => {
        console.log('aaa')
        axios({
            method: 'get',
        })
        .then(res => {
            console.log(res)
        })
        .catch(err => {
            console.log(err)
        })

    })

    return(
        <h1>MyPage</h1>
    )
}

export default MyPage;