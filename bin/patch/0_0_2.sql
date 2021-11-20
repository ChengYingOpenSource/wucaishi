update ep_data_structure set code=md5(code), parent_code=md5(parent_code);
update ep_data_packager set req_datastructure_code=md5(req_datastructure_code), resp_datastructure_code=md5(resp_datastructure_code);
update ep_data_view set req_datastructure_code=md5(req_datastructure_code), resp_datastructure_code=md5(resp_datastructure_code);