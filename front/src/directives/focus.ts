import { Directive } from 'vue'

export const inputFocus: Directive = {
    mounted(el) {
        el.querySelector('input').focus()
        // el.querySelector('input').select()       //不生效
    }
} 