package com.ssafy.yourwine.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ssafy.yourwine.config.security.JwtTokenProvider;
import com.ssafy.yourwine.model.dto.AvgRecoDTO;
import com.ssafy.yourwine.model.dto.FlavorDTO;
import com.ssafy.yourwine.model.dto.PreferenceDTO;
import com.ssafy.yourwine.model.dto.PreferenceRecoDTO;
import com.ssafy.yourwine.model.dto.TasteFilterDTO;
import com.ssafy.yourwine.model.dto.WineDTO;
import com.ssafy.yourwine.model.entity.BatchData;
import com.ssafy.yourwine.model.entity.DislikeFlavor;
import com.ssafy.yourwine.model.entity.Flavor;
import com.ssafy.yourwine.model.entity.LikeFlavor;
import com.ssafy.yourwine.model.entity.TopTen;
import com.ssafy.yourwine.model.entity.User;
import com.ssafy.yourwine.model.entity.Wine;
import com.ssafy.yourwine.model.key.FlavorKey;
import com.ssafy.yourwine.repository.BatchRepository;
import com.ssafy.yourwine.repository.DislikeFlavorRepository;
import com.ssafy.yourwine.repository.LikeFlavorRepository;
import com.ssafy.yourwine.repository.TodayWineRepository;
import com.ssafy.yourwine.repository.TopTenRepository;
import com.ssafy.yourwine.repository.UserRepository;
import com.ssafy.yourwine.repository.WineRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RecoService {

	ModelMapper modelMapper = new ModelMapper();
	private final JwtTokenProvider jwtTokenProvider;
	private final LikeFlavorRepository likeFlavorRepository;
	private final DislikeFlavorRepository dislikeFlavorRepository;
	private final UserRepository userRepository;
	private final TopTenRepository topTenRepository;
	private final WineRepository wineRepository;
	private final TodayWineRepository todayWineRepository;
	private final BatchRepository batchRepository;
	

	public boolean checkPreference(String token) {
		String userId = jwtTokenProvider.getUserId(token);
		User user = userRepository.findByUserId(userId);
		List<LikeFlavor> likeFlavorList = likeFlavorRepository.findByUser(user);


		if(likeFlavorList.size() != 0)
			return true;
		else
			return false;
	}

	public void updatePreference(String token, PreferenceDTO preferenceDTO) {
		String userId = jwtTokenProvider.getUserId(token);
		User user = userRepository.findByUserId(userId);

		likeFlavorRepository.deleteByUser(user);
		dislikeFlavorRepository.deleteByUser(user);

		for (FlavorDTO flavorDTO : preferenceDTO.getLikeList()) {
			LikeFlavor likeFlavor = new LikeFlavor();
			FlavorKey flavorKey = new FlavorKey();
			flavorKey.setUserId(userId);
			flavorKey.setFlavorId(flavorDTO.getFlavorId());
			likeFlavor.setLikeId(flavorKey);
			likeFlavorRepository.save(likeFlavor);
		}

		for (FlavorDTO flavorDTO : preferenceDTO.getDislikeList()) {
			DislikeFlavor dislikeFlavor = new DislikeFlavor();
			FlavorKey flavorKey = new FlavorKey();
			flavorKey.setUserId(userId);
			flavorKey.setFlavorId(flavorDTO.getFlavorId());
			dislikeFlavor.setDislikeId(flavorKey);
			dislikeFlavorRepository.save(dislikeFlavor);
		}

	}

	public void savePreference(String token, PreferenceDTO preferenceDTO) {
		String userId = jwtTokenProvider.getUserId(token);

		for (FlavorDTO flavorDTO : preferenceDTO.getLikeList()) {
			LikeFlavor likeFlavor = new LikeFlavor();
			FlavorKey flavorKey = new FlavorKey();
			flavorKey.setUserId(userId);
			flavorKey.setFlavorId(flavorDTO.getFlavorId());
			likeFlavor.setLikeId(flavorKey);
			likeFlavorRepository.save(likeFlavor);
		}

		for (FlavorDTO flavorDTO : preferenceDTO.getDislikeList()) {
			DislikeFlavor dislikeFlavor = new DislikeFlavor();
			FlavorKey flavorKey = new FlavorKey();
			flavorKey.setUserId(userId);
			flavorKey.setFlavorId(flavorDTO.getFlavorId());
			dislikeFlavor.setDislikeId(flavorKey);
			dislikeFlavorRepository.save(dislikeFlavor);
		}

	}

	public PreferenceDTO getPreference(String token) {
		List<FlavorDTO> likeList = new ArrayList<>();
		List<FlavorDTO> dislikeList = new ArrayList<>();
		PreferenceDTO preferenceDTO = new PreferenceDTO();
		String userId = jwtTokenProvider.getUserId(token);
		User user = userRepository.findByUserId(userId);

		List<LikeFlavor> likeFlavorList = likeFlavorRepository.findByUser(user);
		List<DislikeFlavor> dislikeFlavorList = dislikeFlavorRepository.findByUser(user);


		for(LikeFlavor likeFlavor: likeFlavorList){
			FlavorDTO flavorDTO = modelMapper.map(likeFlavor.getFlavor(), FlavorDTO.class);
			likeList.add(flavorDTO);
		}

		for (DislikeFlavor dislikeFlavor : dislikeFlavorList) {
			FlavorDTO flavorDTO = modelMapper.map(dislikeFlavor.getFlavor(), FlavorDTO.class);
			dislikeList.add(flavorDTO);
		}

		preferenceDTO.setLikeList(likeList);
		preferenceDTO.setDislikeList(dislikeList);

		return preferenceDTO;
	}

	/** ????????? ?????? */
	public PreferenceRecoDTO getWineListByPreference(String token, int page) {
		String userId = jwtTokenProvider.getUserId(token);
		User user = userRepository.findByUserId(userId);

		// ?????? ????????? ?????????
		List<Flavor> userLikeFlavor = likeFlavorRepository.findByUser(user).stream().map(LikeFlavor::getFlavor)
				.collect(Collectors.toList());
		List<Flavor> userDisLikeFlavor = dislikeFlavorRepository.findByUser(user).stream().map(DislikeFlavor::getFlavor)
				.collect(Collectors.toList());

		List<FlavorDTO> userLikeFlavorDto = userLikeFlavor.stream().map(FlavorDTO::new).collect(Collectors.toList());
		List<FlavorDTO> userDisLikeFlavorDto = userDisLikeFlavor.stream().map(FlavorDTO::new)
				.collect(Collectors.toList());
		List<WineDTO> wineDtoList = new ArrayList<WineDTO>();
		PageRequest pageRequest = PageRequest.of(page - 1, 15);

		// 1. ????????? ?????? ???????????? ??????
		if (userLikeFlavor.size() != 0 && userDisLikeFlavor.size() != 0)
			wineDtoList = wineRepository.findAllByFlavorList(userLikeFlavor, userDisLikeFlavor, pageRequest).stream()
					.map(WineDTO::new).collect(Collectors.toList());

		// 2. ???????????? ?????? ???????????? ??????
		if (userLikeFlavor.size() != 0 && userDisLikeFlavor.size() == 0)
			wineDtoList = wineRepository.findAllByLikeFlavor(userLikeFlavor, pageRequest).stream().map(WineDTO::new)
					.collect(Collectors.toList());

		PreferenceRecoDTO preferenceRecoDto = new PreferenceRecoDTO();
		preferenceRecoDto.setLikeFlavorList(userLikeFlavorDto);
		preferenceRecoDto.setDislikeFlavorList(userDisLikeFlavorDto);
		preferenceRecoDto.setWineList(wineDtoList);

		return preferenceRecoDto;
	}

	/** ????????? ?????? + ??? ?????? ?????? */
	public PreferenceRecoDTO getWineListByTaste(String token, TasteFilterDTO taste, int page) {
		String userId = jwtTokenProvider.getUserId(token);
		User user = userRepository.findByUserId(userId);

		int startAcidity = taste.getStartAcidity();
		int startSweet = taste.getStartSweet();
		int startTannin = taste.getStartTannin();
		int startBody = taste.getStartBody();
		int endAcidity = taste.getEndAcidity();
		int endSweet = taste.getEndSweet();
		int endTannin = taste.getEndTannin();
		int endBody = taste.getEndBody();

		// ?????? ????????? ?????????
		List<Flavor> userLikeFlavor = likeFlavorRepository.findByUser(user).stream().map(LikeFlavor::getFlavor)
				.collect(Collectors.toList());
		List<Flavor> userDisLikeFlavor = dislikeFlavorRepository.findByUser(user).stream().map(DislikeFlavor::getFlavor)
				.collect(Collectors.toList());

		List<FlavorDTO> userLikeFlavorDto = userLikeFlavor.stream().map(FlavorDTO::new).collect(Collectors.toList());
		List<FlavorDTO> userDisLikeFlavorDto = userDisLikeFlavor.stream().map(FlavorDTO::new)
				.collect(Collectors.toList());

		List<WineDTO> wineDtoList = new ArrayList<WineDTO>();
		PageRequest pageRequest = PageRequest.of(page - 1, 15);

		// 1. ????????? ?????? ???????????? ?????? + ??? ??????
		if (userLikeFlavor.size() != 0 && userDisLikeFlavor.size() != 0)
			wineDtoList = wineRepository
					.findAllByTaste(userLikeFlavor, userDisLikeFlavor, startAcidity, endAcidity, startSweet, endSweet,
							startBody, endBody, startTannin, endTannin, pageRequest)
					.stream().map(WineDTO::new).collect(Collectors.toList());

		// 2. ???????????? ?????? ???????????? ?????? + ??? ??????
		if (userLikeFlavor.size() != 0 && userDisLikeFlavor.size() == 0)
			wineDtoList = wineRepository
					.findAllByTasteLikeFlavor(userLikeFlavor, startAcidity, endAcidity, startSweet, endSweet, startBody,
							endBody, startTannin, endTannin, pageRequest)
					.stream().map(WineDTO::new).collect(Collectors.toList());

		PreferenceRecoDTO preferenceRecoDto = new PreferenceRecoDTO();
		preferenceRecoDto.setLikeFlavorList(userLikeFlavorDto);
		preferenceRecoDto.setDislikeFlavorList(userDisLikeFlavorDto);
		preferenceRecoDto.setWineList(wineDtoList);

		return preferenceRecoDto;
	}
	
	/** ???????????? ?????? */
	public AvgRecoDTO getWineListByAvg (String token){
		String userId = jwtTokenProvider.getUserId(token);
		User user = userRepository.findByUserId(userId);
		List<WineDTO> wineDtoList = new ArrayList<WineDTO>();
		AvgRecoDTO avgRecoDto = new AvgRecoDTO();
		
		//???????????? ??????
		List<Wine> wineList = wineRepository.findAllByUserReview(userId);
		if(wineList.size() == 0) {
			avgRecoDto.setWriteReview(false);
			return avgRecoDto;
		}
		
		avgRecoDto.setWriteReview(true);
		BatchData batchData = batchRepository.findByUserId(userId);
		
		//????????????????????? ?????? ????????? db??? ?????? ??????
		if(batchData == null) {
			avgRecoDto.setBatchData(false); 
			return avgRecoDto;
		}
		
		String recoString = batchData.getRecoList().replace(" ", "");
		// string -> string[]
		String[] recoStringList = recoString.substring(1, recoString.length() - 1).split(",");
		// string -> int []
		int [] recoListId = Arrays.asList(recoStringList).stream().mapToInt(Integer::parseInt).toArray();
		List<Wine> recoList = wineRepository.findAllByWineList(recoListId);
		List<WineDTO> recoDtoList = new ArrayList<WineDTO>();
		
		//???????????? ?????? -> ???????????? ?????? wine_id????????? ?????????????????? ???????????? ????????? ?????? x 
		for (int i = 0; i < 5; i++) {
			for (Wine wine : recoList) {
				if(wine.getWineId() == recoListId[i]) {
					WineDTO wineDto = modelMapper.map(wine, WineDTO.class);
					recoDtoList.add(wineDto);
				}
			}
		}
		
		//set
		avgRecoDto.setBatchData(true);
		avgRecoDto.setWineDtoList(recoDtoList);
		
		return avgRecoDto;
	}
	

	public List<WineDTO> getTopten(int min, int max) {

		PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("score").descending());
		List<TopTen> topTenList = topTenRepository.findByPriceGreaterThanEqualAndPriceLessThanEqual(min, max,
				pageRequest);
		List<WineDTO> wineDTOList = new ArrayList<>();

		for (TopTen topTen : topTenList) {
			Wine wine = wineRepository.findByWineId(topTen.getWineId());
			WineDTO wineDTO = modelMapper.map(wine, WineDTO.class);
			System.out.println(wineDTO);

			wineDTOList.add(wineDTO);
		}

		return wineDTOList;

	}

	public WineDTO getTodayWine() {

		Wine wine = wineRepository.findByWineId(todayWineRepository.findAll().get(0).getWineId());
		WineDTO wineDTO = modelMapper.map(wine, WineDTO.class);
		return wineDTO;
	}
}
