local product_key = KEYS[1]
local key = KEYS[1] .. "::users"

local exists = redis.call("EXISTS", product_key)
if exists == 1 then
    local stock = redis.call("GET", product_key)
    if tonumber(stock) <= 0 then
        return tostring(0)
    else
        redis.call("DECR", product_key)
        redis.call("RPUSH", key, ARGV[1])
        return tostring(1)
    end
end
