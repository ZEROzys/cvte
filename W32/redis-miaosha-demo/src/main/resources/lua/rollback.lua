local product_key = KEYS[1]
local p_u_key = KEYS[1] .. ":uId:" .. ARGV[1]

local exists_product_key = redis.call("EXISTS", product_key)
local exists_pu_key = redis.call("EXISTS", p_u_key)
if exists_pu_key == 1 then
    local res = redis.call("DEL", p_u_key)
    if res == 1 then
        redis.call("INCR", product_key)
    end
end