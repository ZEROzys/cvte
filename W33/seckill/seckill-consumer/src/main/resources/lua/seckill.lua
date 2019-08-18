local product_key = KEYS[1]
local p_u_key = KEYS[1] .. ":uId:" .. ARGV[1]

local exists = redis.call("EXISTS", product_key)
if exists == 1 then
    local stock = redis.call("GET", product_key)
    if tonumber(stock) <= 0 then
        return tostring(0)
    else
        redis.call("DECR", product_key)
        redis.call("SET", p_u_key, 1)
        return tostring(1)
    end
end
