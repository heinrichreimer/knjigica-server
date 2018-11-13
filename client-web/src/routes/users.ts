import {Router} from "express";

/* GET users listing. */
export let router = Router()
    .get('/', (req, res) => {
        res.send('respond with a resource');
    });
