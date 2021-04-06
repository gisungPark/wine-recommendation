import http from '@/api/http';

export function getWineReviewById(pageNum, wineId) {
    return http.get(`/wine/reivew-list/21`,{
        params: {
            page: pageNum,
        }
    });
}

export function writeReview(contents, cost, point, wineId) {
    return http.post(`/wine/reivew`, { 
        contents: contents,
        cost: cost,
        point: point,
        wineId: wineId,
    });
}
