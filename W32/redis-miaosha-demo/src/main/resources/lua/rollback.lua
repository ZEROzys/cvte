local product_key = KEYS[1]
local list_key = KEYS[1] .. ":users"

local exists_product_key = redis.call("EXISTS", product_key)
local exists_list_key = redis.call("EXISTS", list_key)
if exists_list_key == 1 then
    local res = redis.call("LREM", list_key, 0, ARGV[1])
    if res == 1 then
        redis.call("INCR", product_key)
    end
end