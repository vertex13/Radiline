package xyz.skether.radiline.data.shoutcast

class ShoutcastError : Exception {

    constructor(message: String) : super(message)

    constructor(cause: Throwable) : super(cause)

}
