
import { Directive } from 'vue'



export const analyseDrag: Directive = {
    mounted(el) {
        let oDiv = el
        const element = document.querySelector('.analyseHead') as HTMLElement
        const map = document.querySelector('.container') as HTMLElement

        document.onselectstart = () => {
            return false
        }

        if (element != null) {
            element.onmousedown = function (e: any) {
                let disX = e.clientX - oDiv.offsetLeft;
                let disY = e.clientY - oDiv.offsetTop;

                document.onmousemove = function (de) {
                    let l = de.clientX - disX;
                    let t = de.clientY - disY;
                    if (l >= 0 && t >= 0 && t <= map.offsetHeight - el.offsetHeight && l <= map.offsetWidth - el.offsetWidth) {
                        oDiv.style.left = l + "px";
                        oDiv.style.top = t + "px";
                    } else {
                        if (l < 0) {
                            oDiv.style.left = "0px"
                        }
                        if (t < 0) {
                            oDiv.style.top = "0px"
                        }
                        if (t > map.offsetHeight - el.offsetHeight) {
                            oDiv.style.top = map.offsetHeight - el.offsetHeight + 'px'
                        }
                        if (l > map.offsetWidth - el.offsetWidth) {
                            oDiv.style.left = map.offsetWidth - el.offsetWidth
                        }
                    }

                }
                document.onmouseup = function () {
                    document.onmousemove = null;
                    document.onmouseup = null;
                };
                return false

            }
        }


    }
}

export const drag: Directive = {
    mounted(el, binding) {
        let oDiv = el
        const element = document.querySelector('.dragHead') as HTMLElement
        const map = document.querySelector('.container') as HTMLElement
        document.onselectstart = () => {
            return false
        }

        if (element != null) {
            console.log(element)
            element.onmousedown = function (e: any) {
                console.log(element)
                let disX = e.clientX - oDiv.offsetLeft;
                let disY = e.clientY - oDiv.offsetTop;

                document.onmousemove = function (de) {
                    let l = de.clientX - disX;
                    let t = de.clientY - disY;
                    if (l >= 0 && t >= 0 && t <= map.offsetHeight - el.offsetHeight && l <= map.offsetWidth - el.offsetWidth) {
                        oDiv.style.left = l + "px";
                        oDiv.style.top = t + "px";
                    } else {
                        if (l < 0) {
                            oDiv.style.left = "0px"
                        }
                        if (t < 0) {
                            oDiv.style.top = "0px"
                        }
                        if (t > map.offsetHeight - el.offsetHeight) {
                            oDiv.style.top = map.offsetHeight - el.offsetHeight + 'px'
                        }
                        if (l > map.offsetWidth - el.offsetWidth) {
                            oDiv.style.left = map.offsetWidth - el.offsetWidth
                        }
                    }

                }
                document.onmouseup = function () {
                    document.onmousemove = null;
                    document.onmouseup = null;
                };
                return false

            }
        }


    }
}