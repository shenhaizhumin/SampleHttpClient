package com.zengqi.http_core

import java.lang.Exception

class HttpException(val resp: Response) : Exception(resp.responseMessage()) {

}