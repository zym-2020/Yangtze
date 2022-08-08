export const DEG2RAD: number = Math.PI / 180;

export function clamp(num: number, min: number, max: number) {
    return Math.min(Math.max(num, min), max);
}
