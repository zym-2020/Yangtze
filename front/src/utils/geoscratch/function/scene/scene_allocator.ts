
const invalid_guid = 0;

export class SceneAllocator<T> {
    private elements_guid_map: Map<T, number>;
    private guid_elements_map: Map<number, T>;

    constructor() {
        this.elements_guid_map = new Map();
        this.guid_elements_map = new Map();
    }

    allocGuid(t: T): number {
        const findIt = this.elements_guid_map.get(t);
        if (findIt)
            return findIt;
        
        for (let i = 0; i < this.guid_elements_map.size + 1; i++) {
            const guid = i + 1;
            if (!this.guid_elements_map.has(guid)) {
                this.guid_elements_map.set(guid, t);
                this.elements_guid_map.set(t, guid);
                return guid;
            }
        }
        return invalid_guid;
    }

    getGuidRelatedElement(guid: number) {
        const findIt = this.guid_elements_map.get(guid);
        if (findIt)
            return findIt;
        return null;
    }

    freeGuid(guid: number) {
        const findIt = this.guid_elements_map.get(guid);
        if (findIt) {
            this.elements_guid_map.delete(findIt);
            this.guid_elements_map.delete(guid);
        }
    }

    freeElement(t: T) {
        const findIt = this.elements_guid_map.get(t);
        if (findIt) {
            this.elements_guid_map.delete(t);
            this.guid_elements_map.delete(findIt);
        }
    }

    getAllocateGuids() {
        const allocatedGuids: Array<number> = [];

        for(const element of this.guid_elements_map)
            allocatedGuids.push(element[0]);

        return allocatedGuids;
    }

    clear() {
        this.elements_guid_map.clear();
        this.guid_elements_map.clear();
    }

    static isValidGuid(guid: number) {
        return guid != invalid_guid;
    }
}