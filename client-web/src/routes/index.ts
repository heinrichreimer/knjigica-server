import {Router} from "express";

/* GET home page. */
export let router = Router()
    .get('/', (req, res) => {
        res.render('index', { title: 'Express' });
    });
