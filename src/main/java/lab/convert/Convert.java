package lab.convert;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import java.util.List;
import java.util.stream.Collectors;

public class Convert {

    private ModelMapper modelMapper;

    private Convert(){
        this.modelMapper = new ModelMapper();
        this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    public static Convert getModelMapper() {
        return new Convert();
    }
    public <D,E> D toDto(E entity,Class<D> dto){
        return modelMapper.map(entity,dto);
    }


    public <E,D> List<D> toDtoList(List<E> entityList, Class<D> dto){
        return entityList.stream().map(t->toDto(t,dto)).collect(Collectors.toList());
    }


    public <D,E> E toEntity(D dto, Class<E> entity){
        return modelMapper.map(dto,entity);
    }


    public <D,E> List<E> toEntityList(List<D> dto, Class<E> entity){
        return dto.stream().map(t->toEntity(t,entity)).collect(Collectors.toList());
    }


}



